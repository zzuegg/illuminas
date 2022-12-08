#extension GL_ARB_separate_shader_objects : enable
uniform mat3 g_WorldMatrixInverseTranspose;


#ifdef BASECOLORMAP
uniform sampler2D m_BaseColorMap;
#endif
uniform vec4 m_BaseColor;
#ifdef USE_PACKED_MR
uniform sampler2D m_MetallicRoughnessMap;
#else
#ifdef METALLICMAP
uniform sampler2D m_MetallicMap;
#endif
#ifdef ROUGHNESSMAP
uniform sampler2D m_RoughnessMap;
#endif
#endif
uniform float m_Metallic;
uniform float m_Roughness;

#ifdef EMISSIVE
uniform vec4 m_Emissive;
#endif
#ifdef EMISSIVEMAP
uniform sampler2D m_EmissiveMap;
#endif
#if defined(EMISSIVE) || defined(EMISSIVEMAP)
uniform float m_EmissivePower;
uniform float m_EmissiveIntensity;
#endif
#ifdef NORMALMAP
uniform sampler2D m_NormalMap;
#endif
#ifdef DISCARD_ALPHA
uniform float m_AlphaDiscardThreshold;
#endif

in vec2 texCoord;
in vec3 wNormal;
in vec4 wTangent;
in vec3 wPosition;
layout (location = 0) out vec3 normalOut;
layout (location = 1) out vec3 baseColor;
layout (location = 2) out vec3 metallicRoughness;
layout (location = 3) out vec3 emissive;


void main(){
    vec3 norm = normalize(wNormal);
    vec2 newTexCoord=texCoord;

    #ifdef NORMALMAP
    vec3 tan = normalize(wTangent.xyz);
    vec3 bitan=wTangent.w * cross((norm), (tan));
    mat3 tbnMat = mat3(tan, bitan, norm);
    vec3 normalHeight = texture2D(m_NormalMap, newTexCoord).rgb;
    vec3 normal = normalize((normalHeight.xyz * vec3(2.0, NORMAL_TYPE * 2.0, 2.0) - vec3(1.0, NORMAL_TYPE * 1.0, 1.0)));
    normal = (tbnMat * normal);
    normalOut=normal;
    #else
    normalOut=wNormal;
    #endif

    #ifdef BASECOLORMAP
    vec4 albedo = texture2D(m_BaseColorMap, newTexCoord) * m_BaseColor;
    #else
    vec4 albedo = m_BaseColor;
    #endif
    #ifdef DISCARD_ALPHA
    if(albedo.a < m_AlphaDiscardThreshold){
        discard;
    }
    #endif

    vec2 metallicroughness;
    #ifdef USE_PACKED_MR
    metallicroughness = texture2D(m_MetallicRoughnessMap, newTexCoord).bg;
    metallicroughness.y = metallicroughness.y * max(m_Roughness, 1e-4);
    metallicroughness.x = metallicroughness.x * max(m_Metallic, 0.0);
    #else
    #ifdef ROUGHNESSMAP
    metallicroughness.y = texture2D(m_RoughnessMap, newTexCoord).r * max(m_Roughness, 1e-4);
    #else
    metallicroughness.y =  max(m_Roughness, 1e-4);
    #endif
    #ifdef METALLICMAP
    metallicroughness.x = texture2D(m_MetallicMap, newTexCoord).r * max(m_Metallic, 0.0);
    #else
    metallicroughness.x =  max(m_Metallic, 0.0);
    #endif
    #endif

    #if defined(EMISSIVE) || defined (EMISSIVEMAP)
    #ifdef EMISSIVEMAP
    vec4 emissiveIn = texture2D(m_EmissiveMap, newTexCoord);
    #else
    vec4 emissiveIn = m_Emissive;
    #endif
    emissive = (emissiveIn * pow(emissiveIn.a, m_EmissivePower) * m_EmissiveIntensity).xyz;
    #endif

    baseColor=albedo.xyz;
    metallicRoughness=vec3(metallicroughness, 0);
}