#extension GL_ARB_separate_shader_objects : enable
uniform mat3 g_WorldMatrixInverseTranspose;
uniform sampler2D m_NormalMap;
uniform sampler2D m_BaseColor;
uniform sampler2D m_MetallicRoughness;

in vec2 texCoord;
in vec3 wNormal;
in vec4 wTangent;
in vec3 wPosition;
layout (location = 0) out vec3 normalOut;
layout (location = 1) out vec3 baseColor;
layout (location = 2) out vec3 metallicRoughness;


void main(){
    vec3 norm = normalize(wNormal);
    vec3 tan = normalize(wTangent.xyz);
    vec3 bitan=wTangent.w * cross((norm), (tan));
    mat3 tbnMat = mat3(tan, bitan, norm);
    vec3 normalHeight = texture2D(m_NormalMap, texCoord).rgb;
    normalHeight=vec3(normalHeight.x, 1.0-normalHeight.y, normalHeight.z);
    vec3 normal = normalize((normalHeight.xyz * vec3(2.0, -2.0 * 2.0, 2.0) - vec3(1.0, -2.0 * 1.0, 1.0)));
    normal = (tbnMat * normal);
    normalOut=normal;
    vec4 albedo=texture2D(m_BaseColor, texCoord);
    //if (albedo.a<0.5f){
    //    discard;
    //}
    baseColor=albedo.rgb;
    metallicRoughness=vec3(0, texture2D(m_MetallicRoughness, texCoord).yz);
}