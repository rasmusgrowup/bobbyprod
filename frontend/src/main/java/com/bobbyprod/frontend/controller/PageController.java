package com.bobbyprod.frontend.controller;

import com.bobbyprod.common.Assets.Asset;
import com.bobbyprod.common.Communication.Mediator;
import com.bobbyprod.common.Interfaces.IMediator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PageController {

    @GetMapping("/")
    public String showIndex() {
        return "index"; // This will render index.html template
    }

}
