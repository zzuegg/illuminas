#extension GL_ARB_separate_shader_objects : enable
#import "Shaders/Illuminas/ShaderLib/Lighting.glsllib"
#import "Shaders/Illuminas/ShaderLib/GBuffer.glsllib"
uniform mat4 g_ViewProjectionMatrixInverse;
uniform vec3 g_CameraPosition;

in vec2 fragTexCoord;

uniform vec4 m_AmbientLight;

uniform vec3[LIGHTS] m_LightDirections;
uniform vec3[LIGHTS] m_LightColors;
uniform int m_Lights;

layout (location = 0) out vec4 fragColor;

void main(){
    vec3 worldNormal=texture(m_NormalDepth, fragTexCoord).xyz;
    vec3 worldPos=decodeWorldPosition(g_ViewProjectionMatrixInverse,texture(m_Depth, fragTexCoord).x, fragTexCoord);
    vec4 albedoSpecular=texture(m_AlbedoSpecular, fragTexCoord);
    vec2 diffuseSpecular;
    vec3 lightDirection;
    vec3 lightColor;
    vec3 viewDirection=normalize(g_CameraPosition-worldPos);
    for (int i=0;i<m_Lights;i++){
        lightColor=m_LightColors[i];
        lightDirection=normalize(-m_LightDirections[i]);
        computeLighting(worldNormal, lightDirection, viewDirection, diffuseSpecular);
        fragColor+=vec4((lightColor*albedoSpecular.xyz*diffuseSpecular.x)+(lightColor*albedoSpecular.xyz*diffuseSpecular.y*albedoSpecular.w), 1);
    }
    fragColor+=vec4(albedoSpecular.xyz*m_AmbientLight.xyz, 1);
}