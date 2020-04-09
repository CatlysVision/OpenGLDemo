#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES uTextureSampler;
varying vec2 vTextureCoord;
uniform float uTime;

void main() {
    float lightValue = abs(sin(uTime / 1000.0)) / 4.0;
    vec4 vCameraColor = texture2D(uTextureSampler, vTextureCoord);

    float r = vCameraColor.r;
    float g = vCameraColor.g;
    float b = vCameraColor.b;

    float newR = r + lightValue;
    float newG = g + lightValue;
    float newB = b + lightValue;

    gl_FragColor = vec4(newR, newG, newB, 1.0);
}
