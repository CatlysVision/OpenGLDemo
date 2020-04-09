#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES uTextureSampler;
varying vec2 vTextureCoord;

void main() {
    vec4 vCameraColor = texture2D(uTextureSampler, vTextureCoord);
    float r = vCameraColor.r;
    float g = vCameraColor.g;
    float b = vCameraColor.b;

    float newR = (1.0-r);
    float newG = (1.0-g);
    float newB = (1.0-b);

    gl_FragColor = vec4(newR, newG, newB, 1.0);
}
