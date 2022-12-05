#import "Shaders/Illuminas/ShaderLib/HWSkinning.glsllib"
uniform mat4 g_WorldViewProjectionMatrix;
uniform mat3 g_WorldNormalMatrix;
uniform mat3 g_WorldMatrix;

in vec3 inPosition;
in vec3 inNormal;
in vec4 inTangent;
in vec2 inTexCoord;

out vec2 texCoord;
out vec3 wNormal;
out vec4 wTangent;
out vec3 wPosition;

void main(){
    vec4 modelSpacePos=vec4(inPosition,1.0);
    vec3 modelSpaceNorm=inNormal;
    #ifdef NUM_BONES
    Skinning_Compute(modelSpacePos, modelSpaceNorm);
    #endif
    gl_Position = g_WorldViewProjectionMatrix * modelSpacePos;
    wPosition = g_WorldMatrix*modelSpacePos.xyz;
    wNormal = (g_WorldNormalMatrix * modelSpaceNorm).xyz;
    wTangent = vec4(g_WorldNormalMatrix * inTangent.xyz, inTangent.w);
    texCoord=inTexCoord;
}