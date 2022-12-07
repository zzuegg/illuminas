#extension GL_ARB_separate_shader_objects : enable
#import "Shaders/Illuminas/ShaderLib/BlinnPhong.glsllib"
#import "Shaders/Illuminas/ShaderLib/GBuffer.glsllib"
#import "Shaders/Illuminas/ShaderLib/Shadow.glsllib"
uniform mat4 g_ViewProjectionMatrixInverse;
uniform vec3 g_CameraPosition;
uniform vec2 g_Resolution;
uniform float m_VolumetricIntensity;
in vec3 lightPosition;
in vec3 lightDirection;
in vec3 lightColor;
in vec2 lightAngles;
in float lightRadius;
in float lightAngularFallofFactor;
uniform sampler2D m_NormalDepth;
uniform sampler2D m_AlbedoSpecular;
uniform sampler2D m_Depth;
in vec3 worldPosition;

in vec2 texCoords;
in vec4 gl_FragCoord;


layout (location = 0) out vec4 fragColor;



void main(){
    vec2 fragTexCoord=gl_FragCoord.xy/g_Resolution;
    vec3 depthPosition=decodeWorldPosition(g_ViewProjectionMatrixInverse, texture(m_Depth, fragTexCoord).x, fragTexCoord);
    float cameraToLight=distance(worldPosition, g_CameraPosition);
    float cameraToWorld=distance(depthPosition, g_CameraPosition);

    float fallOff=1;
    if (gl_FrontFacing){
        fragColor=(vec4(lightColor, 1)*m_VolumetricIntensity)*(min(cameraToWorld, cameraToLight)-1.0)*fallOff;
    }
    if (!gl_FrontFacing){
        fragColor=-(vec4(lightColor, 1)*m_VolumetricIntensity)*(min(cameraToWorld, cameraToLight)-1.0)*fallOff;
    }
}