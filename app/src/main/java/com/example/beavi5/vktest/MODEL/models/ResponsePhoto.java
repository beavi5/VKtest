package com.example.beavi5.vktest.MODEL.models;

/**
 * Created by beavi5 on 19.09.2017.
 */

public class ResponsePhoto {

    Response response;


    public ResponsePhoto(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }


    public String  getUploadUrl() {
        return response.getUpload_url();
    }

    class Response {

        String upload_url;
        String aid;
        String  mid;

        public Response(String upload_url, String aid, String mid) {
            this.upload_url = upload_url;
            this.aid = aid;
            this.mid = mid;
        }

        public String getUpload_url() {
            return upload_url;
        }

        public String getAid() {
            return aid;
        }

        public String getMid() {
            return mid;
        }
    }
}
