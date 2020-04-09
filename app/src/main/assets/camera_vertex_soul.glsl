attribute vec4 aPosition;
precision mediump float;
uniform mat4 uTextureMatrix;
attribute vec4 aTextureCoordinate;
varying vec2 vTextureCoord;
attribute float alpha;
varying float inAlpha;

void main() {
    vTextureCoord = (uTextureMatrix * aTextureCoordinate).xy;
    gl_Position =  aPosition;
    inAlpha = alpha;
}