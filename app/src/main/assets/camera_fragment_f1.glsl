#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES uTextureSampler;
varying vec2 vTextureCoord;

void main() {
    vec4 vCameraColor = texture2D(uTextureSampler, vTextureCoord);
    float r = vCameraColor.r;
    float g = vCameraColor.g;
    float b = vCameraColor.b;

    float newR = (0.393 * r + 0.769 * g + 0.189 * b);
    float newG = (0.349 * r + 0.686 * g + 0.168 * b);
    float newB = (0.272 * r + 0.534 * g + 0.131 * b);

    gl_FragColor = vec4(newR, newG, newB, 1.0);
}
