package com.wang.my_community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDto {
    private List<QuestionDto> questionDtos;
    private List<Integer> pages = new ArrayList<>();
    private boolean showNext;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showEndPage;
    private Integer page;
    private  Integer totalPage;


    public void setPagination(Integer totalCount, Integer page, Integer size) {

        if(totalCount%size==0){
            totalPage = totalCount/size;
        }else {
            totalPage = totalCount/size + 1;
        }

        if (page < 1){
            page = 1;
        }
        if (page > totalPage){
            page = totalPage;
        }
        this.page = page;
        pages.add(page);
        for (int i = 1; i <= 3; i++) {
            if(page - i>0){
                pages.add(0,page - i);//????????????????????????????????????
            }
            if(page + i <= totalPage){
                pages.add(page + i);
            }

        }

//      是否展示跳转前一页
//        if(page==1){
//            showPrevious = false;
//        }else {
//            showPrevious = true;
//        }
//        //是否展示跳转后一页
//        if (page==totalPage){
//            showNext = false;
//        }else {
//            showNext = true;
//        }
////      是否展示跳转到第一页
//        if (pages.contains(1)){
//            showFirstPage = false;
//        }else {
//            showFirstPage = true;
//        }
//        //是否展示跳转到最后一页
//        if (pages.contains(totalPage)){
//            showEndPage = false;
//        }else {
//            showEndPage = true;
//        }

    }
}
