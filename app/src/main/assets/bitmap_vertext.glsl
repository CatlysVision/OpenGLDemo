attribute vec4 aPosition;
attribute vec2 aCoordinate;
varying vec2 vCoordinate;
uniform mat4 uModelMatrix;
uniform mat4 uViewMatrix;
uniform mat4 uProjectionMatrix;

void main() {
    gl_Position  = uProjectionMatrix * aPosition;
    vCoordinate = aCoordinate;
}