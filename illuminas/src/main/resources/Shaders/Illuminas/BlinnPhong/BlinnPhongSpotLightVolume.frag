#extension GL_ARB_separate_shader_objects : enable
#import "Shaders/Illuminas/ShaderLib/Lighting.glsllib"
#import "Shaders/Illuminas/ShaderLib/GBuffer.glsllib"
#import "Shaders/Illuminas/ShaderLib/Shadow.glsllib"
uniform mat4 g_ViewProjectionMatrixInverse;
uniform vec3 g_CameraPosition;
uniform vec2 g_Resolution;

in vec3 lightPosition;
in vec3 lightDirection;
in vec3 lightColor;
in vec2 lightAngles;
in float lightRadius;
in float lightAngularFallofFactor;




in vec4 gl_FragCoord;


layout (location = 0) out vec4 fragColor;



void main(){
    vec2 fragTexCoord=gl_FragCoord.xy/g_Resolution;
    vec3 worldPosition=decodeWorldPosition(g_ViewProjectionMatrixInverse, texture(m_Depth, fragTexCoord).x, fragTexCoord);
    vec3 lightDirectionWorld =lightPosition-worldPosition;
    vec3 viewDirection=normalize(g_CameraPosition-worldPosition);
    float lightToWorldDistance = length(lightDirectionWorld);
    lightDirectionWorld = normalize(lightDirectionWorld);


    float linearFallOf = computeLinearFallOf(lightRadius, lightToWorldDistance);
    float currAngleCos = dot(normalize(-lightDirection), normalize(lightDirectionWorld));
    float angularFallOf = computeAngularFallOf(currAngleCos, lightAngles.x, lightAngles.y);

    float shadowFactor=1;
    #if defined SHADOW
        shadowFactor=calculateShadow(worldPosition, vec3(0, 0, 0), lightToWorldDistance)-1;
        vec4 shadowCoordinates=(m_ShadowViewProjectionMatrix*vec4(worldPosition,1.0));
        shadowCoordinates = vec4(((shadowCoordinates.xyz/shadowCoordinates.w)+1.0)/2.0,shadowCoordinates.w);

        if(shadowCoordinates.x<0 || shadowCoordinates.x>1 || shadowCoordinates.y<0 || shadowCoordinates.y> 1){
            angularFallOf=0;
        }else{
            angularFallOf=max(angularFallOf,lightAngularFallofFactor);
            if(shadowCoordinates.w<0){
                angularFallOf=0;
            }
        }
    #endif
    fragColor+=vec4(computeLighting(lightColor,fragTexCoord, lightDirectionWorld, viewDirection, linearFallOf, angularFallOf), 1)*shadowFactor;
}