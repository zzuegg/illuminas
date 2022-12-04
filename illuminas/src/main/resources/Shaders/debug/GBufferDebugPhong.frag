in vec2 fragTexCoord;
uniform sampler2D m_CurrentResult;
uniform sampler2D m_NormalDepth;
uniform sampler2D m_Depth;
uniform sampler2D m_AlbedoSpecular;

out vec4 fragColor;

void main(){
    vec2 sampleTexCoord=fragTexCoord;
    if (fragTexCoord.x>0.8){
        float texId=sampleTexCoord.y*5;
        sampleTexCoord.x=(sampleTexCoord.x-0.8)*5.0;
        sampleTexCoord.y=mod(sampleTexCoord.y*5, 1);
        if (texId>4){
            fragColor=vec4(texture(m_NormalDepth, sampleTexCoord).xyz, 1);
        } else if (texId>3){
            fragColor=vec4(texture(m_NormalDepth, sampleTexCoord).www, 1);
        } else if (texId>2){
            fragColor=vec4(texture(m_Depth, sampleTexCoord).xxx, 1);
        } else if (texId>1){
            fragColor=vec4(texture(m_AlbedoSpecular, sampleTexCoord).xyz, 1);
        } else if (texId>0){
            fragColor=vec4(texture(m_AlbedoSpecular, sampleTexCoord).www, 1);
        }
    } else {
        fragColor=vec4(texture(m_CurrentResult, sampleTexCoord).xyz, 1);
    }
}