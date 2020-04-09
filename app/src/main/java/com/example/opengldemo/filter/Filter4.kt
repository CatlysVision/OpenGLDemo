package com.example.opengldemo.filter

import com.example.opengldemo.manager.*


class Filter4(id: Int) : BaseFilter(id) {

    override fun getParam(): ShaderParam {
        return getParamByType(SHADER_4)
    }

}