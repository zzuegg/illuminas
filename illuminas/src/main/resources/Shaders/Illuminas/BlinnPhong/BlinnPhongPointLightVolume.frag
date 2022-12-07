#extension GL_ARB_separate_shader_objects : enable
#import "Shaders/Illuminas/ShaderLib/Lighting.glsllib"
#import "Shaders/Illuminas/ShaderLib/GBuffer.glsllib"
#import "Shaders/Illuminas/ShaderLib/Shadow.glsllib"
uniform mat4 g_ViewProjectionMatrixInverse;
uniform vec3 g_CameraPosition;
uniform vec2 g_Resolution;

in vec3 lightPosition;
in vec3 lightColor;
in float lightRadius;


in vec4 gl_FragCoord;


layout (location = 0) out vec4 fragColor;



void main(){
    vec2 fragTexCoord=gl_FragCoord.xy/g_Resolution;
    vec3 worldNormal=texture(m_NormalDepth, fragTexCoord).xyz;
    vec3 worldPosition=decodeWorldPosition(g_ViewProjectionMatrixInverse, texture(m_Depth, fragTexCoord).x, fragTexCoord);
    vec3 lightDirection =lightPosition-worldPosition;
    vec3 viewDirection=normalize(g_CameraPosition-worldPosition);
    float lightToWorldDistance = length(lightDirection);
    lightDirection = normalize(lightDirection);


    float linearFallOf = computeLinearFallOf(lightRadius, lightToWorldDistance);

    float shadowFactor=1;
    fragColor+=vec4(computeLighting(lightColor,fragTexCoord, lightDirection, viewDirection, linearFallOf, 1), 1)*shadowFactor;
}