in vec2 fragTexCoord;
uniform vec2 g_Resolution;
uniform sampler2D m_CurrentResult;
uniform float m_Radius;
uniform int m_Samples;
uniform int m_Horizontal;
out vec4 fragColor;

// Kernel generated at: http://dev.theomader.com/gaussian-kernel-calculator/
const float blurKernel[9] = float[](
//0.000229,	0.005977,	0.060598,	0.241732,	0.382928,	0.241732,	0.060598,	0.005977,	0.000229 // sigma = 1.0 - same results as 5x5
0.028532, 0.067234, 0.124009, 0.179044, 0.20236, 0.179044, 0.124009, 0.067234, 0.028532// sigma = 2.0
//0.081812,	0.101701,	0.118804,	0.130417,	0.134535,	0.130417,	0.118804,	0.101701,	0.081812 // sigma = 4.0

);

// precompute manually later! - testing the optimization idea!
const float blurKernelLin[5] = float[] (
blurKernel[0] + blurKernel[1],
blurKernel[2] + blurKernel[3],
blurKernel[4],
blurKernel[5] + blurKernel[6],
blurKernel[7] + blurKernel[8]
);

// precompute manually later! - testing the optimization idea!
const float offsets[5] = float[](
- (4.0 * blurKernel[0] + 3.0 * blurKernel[1]) / blurKernelLin[0],
- (2.0 * blurKernel[2] + 1.0 * blurKernel[3]) / blurKernelLin[1],
blurKernelLin[2], // or blurKernel[4]
(1.0 * blurKernel[5] + 2.0 * blurKernel[6]) / blurKernelLin[3], // same as first, just positive
(3.0 * blurKernel[7] + 4.0 * blurKernel[8]) / blurKernelLin[4]// same as second, just positive
);

// Values and idea based on: http://rastergrid.com/blog/2010/09/efficient-gaussian-blur-with-linear-sampling/
//const float offsets[5] = float[]( -3.2307692308, -1.3846153846, 0.0, 1.3846153846, 3.2307692308 ); // testing linear approach


// for comparison, different sigma than our selected values (approx. 1.7097)
//const float blurKernelDiscrete[9] = float[] (
//  0.0162162162, 0.0540540541, 0.1216216216, 0.1945945946, 0.2270270270, 0.1945945946, 0.1216216216, 0.0540540541, 0.0162162162
//);

//const float blurKernelLin[5] = float[](
//0.0702702703, 0.3162162162, 0.2270270270, 0.3162162162, 0.0702702703
//);



void main(){
    vec2 texCoord=fragTexCoord;
    vec2 radius=1.0/g_Resolution;
    vec4 finalColor = vec4(0.0);
    if (m_Horizontal==1){
        for (int i = -4; i <= 4; i++) {
            finalColor += texture2D(m_CurrentResult, texCoord.xy + i * radius) * blurKernel[i + 4];
        }
    } else {
        for (int i = -2; i <= 2; i++) {
            finalColor += texture2D(m_CurrentResult, texCoord.xy + offsets[i + 2] * radius) * blurKernelLin[i + 2];
        }
    }
    fragColor=finalColor;
}