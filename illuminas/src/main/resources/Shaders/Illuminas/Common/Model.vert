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
    gl_Position = g_WorldViewProjectionMatrix * vec4(inPosition, 1.0);
    wPosition = g_WorldMatrix*inPosition;
    wNormal = (g_WorldNormalMatrix * inNormal).xyz;
    wTangent = vec4(g_WorldNormalMatrix * inTangent.xyz, inTangent.w);
    texCoord=inTexCoord;
}