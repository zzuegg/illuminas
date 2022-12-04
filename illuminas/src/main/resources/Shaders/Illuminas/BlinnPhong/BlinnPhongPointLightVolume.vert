uniform mat4 g_ViewProjectionMatrix;
uniform vec3 g_CameraPosition;

in vec3 inPosition;

uniform vec3[LIGHTS] m_LightPositions;
uniform vec3[LIGHTS] m_LightColors;
uniform float[LIGHTS] m_LightRadii;
uniform int m_Lights;

out vec3 lightPosition;
out vec3 lightColor;
out float lightRadius;

void main(){
    int lightId=int(gl_VertexID/144);
    lightPosition=m_LightPositions[lightId];
    lightColor=m_LightColors[lightId];
    lightRadius=m_LightRadii[lightId];
    gl_Position=g_ViewProjectionMatrix*vec4(lightPosition+(inPosition*(lightRadius)), 1);
}