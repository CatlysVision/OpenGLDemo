uniform mat4 uMVPMatrix;
attribute vec4 a_Position;
attribute vec4 u_Color;
varying vec4 vColor;

void main() {
    gl_Position  = uMVPMatrix * a_Position;
    vColor = u_Color;
}