#extension GL_ARB_separate_shader_objects : enable
#import "Shaders/Illuminas/ShaderLib/Lighting.glsllib"
#import "Shaders/Illuminas/ShaderLib/GBuffer.glsllib"
#import "Shaders/Illuminas/ShaderLib/Shadow.glsllib"
uniform mat4 g_ViewProjectionMatrixInverse;
uniform vec3 g_CameraPosition;

in vec2 fragTexCoord;

uniform vec4 m_AmbientLight;
#if defined SHADOW
uniform vec3[LIGHTS] m_LightPositions;
#endif
uniform vec3[LIGHTS] m_LightDirections;
uniform vec3[LIGHTS] m_LightColors;
uniform int m_Lights;

layout (location = 0) out vec4 fragColor;

void main(){
    vec3 worldPos=decodeWorldPosition(g_ViewProjectionMatrixInverse, texture(m_Depth, fragTexCoord).x, fragTexCoord);
    vec3 lightDirection;
    vec3 lightColor;
    vec3 viewDirection=normalize(g_CameraPosition-worldPos);
    float shadowFactor=1;
    #if defined SHADOW
        float lightToWorldDistance=distance(m_LightPositions[0], worldPos);
        shadowFactor=calculateShadow(worldPos, vec3(0, 0, 0), lightToWorldDistance)-1;
        //shadowFactor=0;
        //sfragColor+=vec4(1,0,0,0);
    #endif

    for (int i=0;i<m_Lights;i++){
        lightColor=m_LightColors[i];
        lightDirection=normalize(-m_LightDirections[i]);
        //computeLighting(worldNormal, lightDirection, viewDirection, diffuseSpecular);
        fragColor+=shadowFactor*vec4(computeLighting(lightColor, fragTexCoord, lightDirection, viewDirection, 1, 1), 1);
    }

    #if (LIGHT_MODE == 1)
    vec4 albedoSpecular=texture(m_AlbedoSpecular, fragTexCoord);
    #endif
    #if (LIGHT_MODE == 2)
    vec4 albedoSpecular=texture(m_BaseColor, fragTexCoord);
    #endif
    fragColor+=vec4(albedoSpecular.xyz*m_AmbientLight.xyz, 1);


}