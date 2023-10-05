package board.board.common;

import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import board.board.entity.BoardFileEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import board.board.dto.BoardFileDto;

// 해당 클래스를 스프링의 빈으로 등록
@Component
public class FileUtils {

    public List<BoardFileEntity> parseFileInfo(MultipartHttpServletRequest multipartHttpServletRequest) throws Exception{
        // JPA의 @OneToMany 어노테이션으로 인해 첨부파일 클래스(BoardFileEntity)에 게시글 번호를 따로 저장할 필요가 없으므로 parseFileInfo 메서드의 파라미터에 게시글 번호를 받지 않음
        if(ObjectUtils.isEmpty(multipartHttpServletRequest)){
            return null;
        }

        List<BoardFileEntity> fileList = new ArrayList<>();
        // 파일이 업로드될 폴더 생성
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
        ZonedDateTime current = ZonedDateTime.now();
        String path = "images/"+current.format(format);
        File file = new File(path);
        if(file.exists() == false){
            file.mkdirs();
        }

        Iterator<String> iterator = multipartHttpServletRequest.getFileNames();

        String newFileName, originalFileExtension, contentType;

        while(iterator.hasNext()){
            List<MultipartFile> list = multipartHttpServletRequest.getFiles(iterator.next());
            for (MultipartFile multipartFile : list){
                if(multipartFile.isEmpty() == false){
                    // 파일 형식을 확인하고 그에 따른 이미지 확장자 지정
                    contentType = multipartFile.getContentType();
                    if(ObjectUtils.isEmpty(contentType)){
                        break;
                    }
                    else{
                        if(contentType.contains("image/jpeg")) {
                            originalFileExtension = ".jpg";
                        }
                        else if(contentType.contains("image/png")) {
                            originalFileExtension = ".png";
                        }
                        else if(contentType.contains("image/gif")) {
                            originalFileExtension = ".gif";
                        }
                        else{
                            break;
                        }
                    }
                    // 서버에 저장될 파일 이름 생성
                    newFileName = Long.toString(System.nanoTime()) + originalFileExtension;

                    // DB에 저장할 파일 정보를 BoardFileDto에 저장
                    BoardFileEntity boardFile = new BoardFileEntity();
                    // boardFile.setBoardIdx(boardIdx);
                    boardFile.setFileSize(multipartFile.getSize());
                    boardFile.setOriginalFileName(multipartFile.getOriginalFilename());
                    boardFile.setStoredFilePath(path + "/" + newFileName);
                    boardFile.setCreatorId("admin");
                    fileList.add(boardFile);

                    // 업로드된 파일을 새로운 이름으로 바꾸어 지정된 경로에 저장
                    file = new File(path + "/" + newFileName);
                    multipartFile.transferTo(file);
                }
            }
        }
        return fileList;
    }
}