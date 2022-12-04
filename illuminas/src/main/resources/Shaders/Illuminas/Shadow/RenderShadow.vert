uniform mat3 g_WorldMatrix;
uniform mat4 g_WorldViewProjectionMatrix;
in vec3 inPosition;
in vec2 inTexCoord;
out vec3 wPosition;
out vec4 vpPosition;
out vec2 texCoord;
void main()
{
    wPosition = g_WorldMatrix*inPosition;
    vpPosition = g_WorldViewProjectionMatrix * vec4(inPosition, 1);
    texCoord=inTexCoord;
    gl_Position = vpPosition;


}