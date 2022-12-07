uniform mat4 g_ViewProjectionMatrix;
const float offsetMod[5]=float[5](0, 1, 1, 1, 1);
const vec3 upMod[5]=vec3[5](vec3(0), vec3(1), vec3(1), vec3(-1), vec3(-1));
const vec3 leftMod[5]=vec3[5](vec3(0), vec3(1), vec3(-1), vec3(-1), vec3(1));
const int id[18]=int[18](0, 1, 2, 0, 2, 3, 0, 3, 4, 0, 4, 1, 1, 3, 2, 4, 3, 1);


in vec3 inPosition;

uniform vec3[LIGHTS] m_LightPositions;
uniform vec3[LIGHTS] m_LightDirections;
uniform vec3[LIGHTS] m_LightColors;
uniform vec2[LIGHTS] m_LightAngles;
uniform float[LIGHTS] m_LightRadii;
uniform float m_LightAngularFallofFactor;
uniform int m_Lights;

out vec3 lightPosition;
out vec3 lightDirection;
out vec3 lightColor;
out vec2 lightAngles;
out float lightRadius;
out float lightAngularFallofFactor;
void main(){
    int vertexId=id[int(mod(gl_VertexID, 18))];
    int lightId=int(gl_VertexID/18);

    lightPosition=m_LightPositions[lightId];
    lightDirection=normalize(m_LightDirections[lightId]);
    lightColor=m_LightColors[lightId];
    lightRadius=m_LightRadii[lightId];
    lightAngles=m_LightAngles[lightId];
    lightAngularFallofFactor=m_LightAngularFallofFactor;
    vec3 left=normalize(cross(lightDirection, vec3(0, 1, 0)));
    if (abs(lightDirection)==vec3(0, 1, 0)){
        left=normalize(cross(lightDirection, vec3(1, 0, 0)));
    }

    vec3 up=cross(left, lightDirection);



    vec3 mod= (left * leftMod[vertexId])+(up * upMod[vertexId]);

    vec3 origin=lightPosition;
    vec3 centerPoint=origin + (offsetMod[vertexId] * lightDirection * lightRadius);
    float angleFactor=tan(lightAngles.x);
    vec3 worldPosition=centerPoint+mod*lightRadius*angleFactor;
    gl_Position=g_ViewProjectionMatrix*vec4(worldPosition, 1);
}