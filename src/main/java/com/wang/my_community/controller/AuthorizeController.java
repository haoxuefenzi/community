package com.wang.my_community.controller;

import com.wang.my_community.dto.AccessTokenDto;
import com.wang.my_community.dto.GithubUser;
import com.wang.my_community.mapper.UserMapper;
import com.wang.my_community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller

public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.Client.id}")
    private String clientID;
    @Value("${github.Client.secret}")
    private String clientSecret;
    @Value("${github.Redirect.uri}")
    private String clientUri;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request, HttpServletResponse response) {
        AccessTokenDto accessTokenDto = new AccessTokenDto();

        accessTokenDto.setClient_id(clientID);
        accessTokenDto.setClient_secret(clientSecret);
        accessTokenDto.setCode(code);
        accessTokenDto.setRedirect_uri(clientUri);
        accessTokenDto.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDto);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if(githubUser!=null){

            com.wang.my_community.model.User user = new com.wang.my_community.model.User();
            user.setName(githubUser.getLogin());
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setAvatarUrl(githubUser.getAvatarUrl());
            user.setNodeId(githubUser.getNodeId());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.create(user);
            request.getSession().setAttribute("user",githubUser);

            response.addCookie(new Cookie("token",token));
            return "redirect:/";
        }else {
            return "redirect:/";
        }
    }
}
