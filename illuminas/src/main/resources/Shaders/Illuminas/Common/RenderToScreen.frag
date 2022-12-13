in vec2 fragTexCoord;
uniform sampler2D m_CurrentResult;

out vec4 fragColor;

void main(){
    float exposure = 1f;
    const float gamma = 1;
    vec3 hdrColor = texture(m_CurrentResult, fragTexCoord).rgb;

    // exposure tone mapping
    vec3 mapped = vec3(1.0) - exp(-hdrColor * exposure);
    // gamma correction
    mapped = pow(mapped, vec3(1.0 / gamma));

    fragColor = vec4(mapped, 1.0);
}