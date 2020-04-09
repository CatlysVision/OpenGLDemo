#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES uTextureSampler;
varying vec2 vTextureCoord;
const float threshold = 0.33;

void main() {
    vec4 vCameraColor = texture2D(uTextureSampler, vTextureCoord);
    float r = vCameraColor.r;
    float g = vCameraColor.g;
    float b = vCameraColor.b;

    g = r * 0.3 + g * 0.59 + b * 0.11;
    g = g <= threshold ? 0.0 : 1.0;

    gl_FragColor = vec4(vec3(g), 1.0);
}
