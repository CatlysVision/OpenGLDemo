#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES uTextureSampler;
varying vec2 vTextureCoord;

void main() {
    vec4 vCameraColor = texture2D(uTextureSampler, vTextureCoord);
    float r = vCameraColor.r;
    float g = vCameraColor.g;
    float b = vCameraColor.b;

    float newR = abs(g - b + g + r) * r;
    float newG = abs(b - g + b + r) * r;
    float newB = abs(b - g + b + r) * g;

    gl_FragColor = vec4(newR, newG, newB, 1.0);
}
