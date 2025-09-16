package com.itravel.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UploadResult {
    private String fileName;
    private String publicUrl;
    private String path;
    private long size;

    public String getPublicUrl() {
        return this.publicUrl;
    }
}
