package com.example.opengldemo.filter

import com.example.opengldemo.manager.SHADER_BASE
import com.example.opengldemo.manager.ShaderParam
import com.example.opengldemo.manager.getParamByType


class NormalFilter(id: Int) : BaseFilter(id) {

    override fun getParam(): ShaderParam {
        return getParamByType(SHADER_BASE)
    }

}