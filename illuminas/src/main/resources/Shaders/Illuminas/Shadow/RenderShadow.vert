#import "Shaders/Illuminas/ShaderLib/HWSkinning.glsllib"
uniform mat3 g_WorldMatrix;
uniform mat4 g_WorldViewProjectionMatrix;
in vec3 inPosition;
in vec3 inNormal;
in vec2 inTexCoord;
out vec3 wPosition;
out vec4 vpPosition;
out vec2 texCoord;
void main()
{
    vec4 modelSpacePos=vec4(inPosition,1.0);
    vec3 modelSpaceNorm=inNormal;
    #ifdef NUM_BONES
    Skinning_Compute(modelSpacePos, modelSpaceNorm);
    #endif
    wPosition = g_WorldMatrix*modelSpacePos.xyz;
    vpPosition = g_WorldViewProjectionMatrix * modelSpacePos;
    texCoord=inTexCoord;
    gl_Position = vpPosition;


}