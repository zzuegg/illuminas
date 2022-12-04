#extension GL_ARB_separate_shader_objects : enable
#import "Shaders/Illuminas/ShaderLib/BlinnPhong.glsllib"
#import "Shaders/Illuminas/ShaderLib/GBuffer.glsllib"
#import "Shaders/Illuminas/ShaderLib/Shadow.glsllib"
uniform mat4 g_ViewProjectionMatrixInverse;
uniform vec3 g_CameraPosition;
uniform vec2 g_Resolution;

in vec3 lightPosition;
in vec3 lightColor;
in float lightRadius;

uniform sampler2D m_NormalDepth;
uniform sampler2D m_AlbedoSpecular;
uniform sampler2D m_Depth;


in vec4 gl_FragCoord;


layout (location = 0) out vec4 fragColor;



void main(){
    vec2 fragTexCoord=gl_FragCoord.xy/g_Resolution;
    vec3 worldNormal=texture(m_NormalDepth, fragTexCoord).xyz;
    vec3 worldPosition=decodeWorldPosition(g_ViewProjectionMatrixInverse, texture(m_Depth, fragTexCoord).x, fragTexCoord);
    vec4 albedoSpecular=texture(m_AlbedoSpecular, fragTexCoord);
    vec3 lightDirectionWorld =lightPosition-worldPosition;
    vec3 viewDirection=normalize(g_CameraPosition-worldPosition);
    float lightToWorldDistance = length(lightDirectionWorld);
    lightDirectionWorld = normalize(lightDirectionWorld);

    vec2 diffuseSpecular;
    computeLighting(worldNormal, lightDirectionWorld, viewDirection, diffuseSpecular);
    float linearFallOf = computeLinearFallOf(lightRadius, lightToWorldDistance);

    float shadowFactor=1;
    #if defined SHADOWCUBE
    shadowFactor=calculateShadow(worldPosition, lightDirectionWorld, lightToWorldDistance)-1;
    #endif

    vec4 diffuseColor=vec4(linearFallOf*diffuseSpecular.x*albedoSpecular.yxz*lightColor, 1.0);
    vec4 specularColor=vec4(linearFallOf*diffuseSpecular.y*albedoSpecular.w*lightColor, 1.0);
    fragColor=diffuseColor+specularColor*shadowFactor;
}