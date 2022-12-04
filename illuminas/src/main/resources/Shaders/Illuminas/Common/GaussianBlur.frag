in vec2 fragTexCoord;
uniform vec2 g_Resolution;
uniform sampler2D m_CurrentResult;
uniform vec2 m_Direction;
out vec4 fragColor;


vec4 blur13(sampler2D image, vec2 uv, vec2 resolution, vec2 direction) {
    vec4 color = vec4(0.0);
    vec2 off1 = vec2(1.411764705882353) * direction;
    vec2 off2 = vec2(3.2941176470588234) * direction;
    vec2 off3 = vec2(5.176470588235294) * direction;
    color += texture2D(image, uv) * 0.1964825501511404;
    color += texture2D(image, uv + (off1 / resolution)) * 0.2969069646728344;
    color += texture2D(image, uv - (off1 / resolution)) * 0.2969069646728344;
    color += texture2D(image, uv + (off2 / resolution)) * 0.09447039785044732;
    color += texture2D(image, uv - (off2 / resolution)) * 0.09447039785044732;
    color += texture2D(image, uv + (off3 / resolution)) * 0.010381362401148057;
    color += texture2D(image, uv - (off3 / resolution)) * 0.010381362401148057;
    return color;
}


vec4 blur2(sampler2D image, vec2 uv, vec2 resolution, vec2 direction){
    float xs=resolution.x;
    float ys=resolution.y;
    float r=5;
    vec2 pos=uv;
    float x,y,rr=r*r,d,w,w0;


    vec2 p=0.5*(vec2(1.0,1.0)+pos);
    vec4 col=vec4(0.0,0.0,0.0,0.0);
    w0=0.5135/pow(r,0.96);
    if (direction==vec2(1,0)) for (d=1.0/xs,x=-r,p.x+=x*d;x<=r;x++,p.x+=d){ w=w0*exp((-x*x)/(2.0*rr)); col+=texture2D(image,p)*w; }
    if (direction==vec2(0,1))for (d=1.0/ys,y=-r,p.y+=y*d;y<=r;y++,p.y+=d){ w=w0*exp((-y*y)/(2.0*rr)); col+=texture2D(image,p)*w; }
    return col;
}


void main(){
    fragColor=blur13(m_CurrentResult,fragTexCoord,g_Resolution,m_Direction);
}