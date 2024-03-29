package com.github.ltprc.gamepal.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.ltprc.gamepal.entity.UserCharacter;
import com.github.ltprc.gamepal.model.UserData;
import com.github.ltprc.gamepal.repository.UserCharacterRepository;
import com.github.ltprc.gamepal.util.ServerUtil;

@RestController
@RequestMapping(ServerUtil.API_PATH)
public class CharacterController {

    @Autowired
    UserCharacterRepository userCharacterRepository;

    @Deprecated
    @RequestMapping(value = "/get-user-character", method = RequestMethod.POST)
    public ResponseEntity<String> getUserCharacter(HttpServletRequest request) {
        JSONObject rst = new JSONObject();
        String uuid;
        try {
            JSONObject jsonObject = ServerUtil.strRequest2JSONObject(request);
            if (null == jsonObject || !jsonObject.containsKey("body")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
            }
            JSONObject body = (JSONObject) JSONObject.parse((String) jsonObject.get("body"));
            uuid = body.get("uuid").toString();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
        }
        List<UserCharacter> characters = userCharacterRepository.queryUserCharacterByUuid(uuid);
        rst.put("characters", characters);
        return ResponseEntity.status(HttpStatus.OK).body(rst.toString());
    }

    @RequestMapping(value = "/set-user-character", method = RequestMethod.POST)
    public ResponseEntity<String> setUserCharacter(HttpServletRequest request) {
        JSONObject rst = new JSONObject();
        String userCode;
        UserCharacter userCharacter = new UserCharacter();
        try {
            JSONObject jsonObject = ServerUtil.strRequest2JSONObject(request);
            if (null == jsonObject || !jsonObject.containsKey("body")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
            }
            JSONObject body = (JSONObject) JSONObject.parse((String) jsonObject.get("body"));
            userCharacter.setUuid(body.get("uuid").toString());
            userCharacter.setFirstName(body.get("firstName").toString());
            userCharacter.setLastName(body.get("lastName").toString());
            if (StringUtils.isBlank(body.get("nickname").toString())) {
                userCharacter.setNickname(userCharacter.getFirstName() + " " + userCharacter.getLastName());
            } else {
                userCharacter.setNickname(body.get("nickname").toString());
            }
            userCharacter.setNameColor(body.get("nameColor").toString());
            userCharacter.setCreature(body.get("creature").toString());
            userCharacter.setGender(body.get("gender").toString());
            userCharacter.setSkinColor(body.get("skinColor").toString());
            userCharacter.setHairstyle(body.get("hairstyle").toString());
            userCharacter.setHairColor(body.get("hairColor").toString());
            userCharacter.setEyes(body.get("eyes").toString());
            userCharacter.setAvatar(body.getInteger("avatar"));
            userCode = body.get("uuid").toString();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Operation failed");
        }
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        if (userCharacterRepository.queryUserCharacterByUuid(userCode).isEmpty()) {
            userCharacter.setOutfit("");
            userCharacter.setCreateTime(sdf.format(new Date()));
            userCharacter.setUpdateTime(userCharacter.getCreateTime());
        } else {
            UserCharacter existedUserCharacter = userCharacterRepository.queryUserCharacterByUuid(userCode).get(0);
            userCharacter.setOutfit(existedUserCharacter.getOutfit());
            userCharacter.setCreateTime(existedUserCharacter.getCreateTime());
            userCharacter.setUpdateTime(sdf.format(new Date()));
            userCharacterRepository.delete(existedUserCharacter);
        }
        userCharacterRepository.save(userCharacter);
        UserData userData = ServerUtil.userDataMap.get(userCode);
        userData.setFirstName(userCharacter.getFirstName());
        userData.setLastName(userCharacter.getLastName());
        userData.setNickname(userCharacter.getNickname());
        userData.setNameColor(userCharacter.getNameColor());
        userData.setCreature(userCharacter.getCreature());
        userData.setGender(userCharacter.getGender());
        userData.setSkinColor(userCharacter.getSkinColor());
        userData.setHairstyle(userCharacter.getHairstyle());
        userData.setHairColor(userCharacter.getHairColor());
        userData.setEyes(userCharacter.getEyes());
        String outfitsStr = userCharacter.getOutfit();
        Set<String> outfits = new HashSet<>();
        if (StringUtils.isNotBlank(outfitsStr)) {
            String[] outfitsStrs = userCharacter.getOutfit().split(",");
            for (String str : outfitsStrs) {
                outfits.add(str);
            }
        }
        userData.setOutfits(outfits);
        userData.setAvatar(userCharacter.getAvatar());
        return ResponseEntity.status(HttpStatus.OK).body(rst.toString());
    }
}
