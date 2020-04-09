package com.example.opengldemo.filter

import com.example.opengldemo.manager.*


class Filter7(id: Int) : BaseFilter(id) {

    override fun getParam(): ShaderParam {
        return getParamByType(SHADER_7)
    }

}