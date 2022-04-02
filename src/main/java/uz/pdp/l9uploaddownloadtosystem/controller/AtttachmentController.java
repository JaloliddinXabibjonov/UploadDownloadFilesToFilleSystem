package uz.pdp.l9uploaddownloadtosystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.pdp.l9uploaddownloadtosystem.entity.Attachment;
import uz.pdp.l9uploaddownloadtosystem.repository.AttachmentRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController

public class AtttachmentController {
    @Autowired
    AttachmentRepository attachmentRepository;
    public static final String uploadDirectory="Yuklash uchun";
    @PostMapping("/uploadToSystem")
    public String uploadToSystem(MultipartHttpServletRequest request) throws IOException {
        Iterator<String> fileNames = request.getFileNames();
        MultipartFile file = request.getFile(fileNames.next());
        if (file!=null){
            String[] split = file.getOriginalFilename().split("\\.");
            String name= UUID.randomUUID()+"."+split[split.length-1];
            Attachment attachment=new Attachment(file.getOriginalFilename(), file.getSize(), file.getContentType(),name);
            attachmentRepository.save(attachment);
            Path path= Paths.get(uploadDirectory+"/"+name);
            Files.copy(file.getInputStream(), path);
            return "Fayl System ga saqlandi";
        }
        return "Saqlanmadi";
    }

    @GetMapping("/attachment")
    public List<Attachment> getAttachment(){
        return attachmentRepository.findAll();
    }

    @GetMapping("/download/{id}")
    public void downloadFromSystem(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        Optional<Attachment> attachmentRepositoryById = attachmentRepository.findById(id);
        if (attachmentRepositoryById.isPresent()){
            Attachment attachment = attachmentRepositoryById.get();
            response.setHeader("Content-Disposition", "attachment; fileName=\""+attachment.getFileOriginalName()+"\"");
            response.setContentType(attachment.getContentType());
            FileInputStream fileInputStream=new FileInputStream(uploadDirectory+"/"+attachment.getName());
            FileCopyUtils.copy(fileInputStream, response.getOutputStream());
        }
    }
}
