package com.example.opengldemo.filter

import com.example.opengldemo.manager.*


class Filter5(id: Int) : BaseFilter(id) {

    override fun getParam(): ShaderParam {
        return getParamByType(SHADER_5)
    }

}