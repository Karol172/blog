package com.karol172.blog.controllers;

import com.karol172.blog.form.FileForm;
import com.karol172.blog.service.DataPageService;
import com.karol172.blog.service.FilesService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FileController
{
  @Autowired
  private DataPageService dataPageService;
  @Autowired
  private FilesService filesService;
  
  @RequestMapping(value={"/images/{filename:.+}"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  @ResponseBody
  public ResponseEntity<Resource> image(@PathVariable String filename)
  {
    return this.filesService.getImage(filename);
  }
  
  @RequestMapping(value={"/admin/files"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String filesReview(Model model, HttpServletRequest request)
  {
    this.dataPageService.getDataPage(model, request);
    this.filesService.getImages(model);
    return "filesReview";
  }
  
  @RequestMapping(value={"/admin/files/add"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public String addFile(@ModelAttribute("fileForm") @Valid FileForm fileForm, BindingResult result, Model model, HttpServletRequest request)
  {
    this.dataPageService.getDataPage(model, request);
    this.filesService.addImage(fileForm, model, result, request);
    return "filesReview";
  }
  
  @RequestMapping(value={"/admin/files/remove/{id}"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String removeFile(@PathVariable("id") long id, Model model, HttpServletRequest request)
  {
    this.dataPageService.getDataPage(model, request);
    this.filesService.removeImage(id, model);
    return "filesReview";
  }
}
