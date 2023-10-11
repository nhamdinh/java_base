package com.lekwacious.employee_app.model.request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UploadFileRequest {
    //1: avatar, 2: logo 3: upload ảnh product 4: update banner image 5: upload question file 6. upload answer image
    private int type;
    private int typeId;
}
