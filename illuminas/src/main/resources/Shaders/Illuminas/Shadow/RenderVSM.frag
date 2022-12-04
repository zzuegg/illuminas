in vec3 wPosition;
in vec4 vpPosition;
in vec2 texCoord;
uniform sampler2D m_AlbedoMap;
out vec4 fragColor;

void main()
{
    float alpha=texture2D(m_AlbedoMap, texCoord).a;
    if (alpha<0.1){
        discard;
    }
    float depth = vpPosition.z / vpPosition.w;
    depth = (depth +1.0f)/2.0f;//Don't forget to move away from unit cube ([-1,1]) to [0,1] coordinate system

    float moment1 = depth;
    float moment2 = depth * depth;

    // Adjusting moments (this is sort of bias per pixel) using partial derivative
    float dx = dFdx(depth);
    float dy = dFdy(depth);
    moment2 += 0.25*(dx*dx+dy*dy);


    fragColor = vec4(moment1, moment2, 0.0, 0.0);
}