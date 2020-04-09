#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES uTextureSampler;
varying vec2 vTextureCoord;
varying float inAlpha;
uniform float progress;
uniform int drawFBO;
uniform sampler2D uSoulTexture;

void main() {
    float alpha = 0.6 * (1.0 - progress);
    float scale = 1.0 + 0.5 * progress;

    float soulX = 0.5 + (vTextureCoord.x - 0.5) / scale;
    float soulY = 0.5 + (vTextureCoord.y - 0.5) / scale;
    vec2 soulTextureCoords = vec2(soulX, soulY);
    vec4 soulMask = texture2D(uSoulTexture, soulTextureCoords);
    vec4 color = texture2D(uTextureSampler, vTextureCoord);
    if (drawFBO == 0) {
        gl_FragColor = color * (1.0 - alpha) + soulMask * alpha;
    } else {
        gl_FragColor = vec4(color.r, color.g, color.b, inAlpha);
    }
}
