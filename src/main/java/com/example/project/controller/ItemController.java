package com.example.project.controller;


import com.example.project.entity.*;
import com.example.project.service.*;
import com.example.project.entity.Collection;
import com.example.project.entity.Item;
import com.example.project.entity.Role;
import com.example.project.entity.Tag;
import com.example.project.service.CollectionService;
import com.example.project.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

@Controller
public class ItemController {

    @Autowired
    CollectionService collectionService;

    @Autowired
    ItemService itemService;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;
    @Autowired
    TagService tagService;
    @Autowired
    UserService userService;

//    @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
//    @GetMapping("/profile/{uid}/collections/{id}/items")
//    public String getCollectionItems(@PathVariable("id") int id, @PathVariable("uid") Long uid,Model model){
//        Collection collection = collectionService.get(id);
//        List<Item> items = itemService.getByCollectionId(id);
//        model.addAttribute("items", items);
//        model.addAttribute("collection", collection);
//        return "collection_items";
//    }

    @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
    @RequestMapping(value = "/profile/{uid}/collections/{id}/action", method = RequestMethod.GET, params = "add")
    public String newItem(@PathVariable("id") int id,@PathVariable("uid") Long uid, Model model){
        model.addAttribute("filter", new Filter());
        Item item=new Item();
        item.setCollection(collectionService.get(id));
        item.setName("");
        itemService.update(item);
        model.addAttribute("item", item);
        System.out.println(item.getId());
        model.addAttribute("itemId", item.getId());
        model.addAttribute("other", "");
        return "new_item";
    }

    @GetMapping("/profile/{uid}/collections/{id}/view_item/{item_id}")
    public String viewItem(Model model,@PathVariable("item_id") int itemId)
    {
        model.addAttribute("filter", new Filter());
        Item item=itemService.get(itemId);
        Comment comment=new Comment();
        List<Comment> comments=commentService.commentList(itemId);
        model.addAttribute("item",item);
        model.addAttribute("like",new Like());
        List<Like> likes=likeService.likeList(itemId);
        Integer size=likes.size();
        model.addAttribute("size",size);
        model.addAttribute("curcomment",comment);
        model.addAttribute("comments",comments);
        model.addAttribute("liked",checkLike(itemId));
        return "view_item";
    }


    @PreAuthorize("#user== authentication.principal.getId()")

    @RequestMapping(value = "/profile/{uid}/collections/{id}/view_item/{item_id}/action", method = {RequestMethod.POST,RequestMethod.GET}, params = "save")
    //ResponseBody
    public String saveComment(@PathVariable("item_id") int itemId,  @PathVariable("uid") Long uid,
                              @RequestParam("user") Long user,
                              @RequestParam("commentId") int commentId,@ModelAttribute Comment comment){
        Comment curcomment=commentService.get(commentId);
        curcomment.setItem(itemService.get(itemId));
        curcomment.setCreationDate(new Timestamp(System.currentTimeMillis()));
        curcomment.setUser(userService.get(uid));
        commentService.save(comment);
        return "redirect:/profile/{uid}/collections/{id}/view_item/{item_id}";
    }

    @PreAuthorize("#user== authentication.principal.getId()")
    @RequestMapping(value = "/profile/{uid}/collections/{id}/view_item/{item_id}/action", method = {RequestMethod.POST,RequestMethod.GET}, params = "delete")
    public String deleteComment(@PathVariable("item_id") int itemId,  @PathVariable("uid") Long uid,
                                @RequestParam("user") Long user,
                                @RequestParam("commentId") int commentId){
        commentService.delete(commentId);
        return "redirect:/profile/{uid}/collections/{id}/view_item/{item_id}";
    }

    public boolean checkLike( int itemId)
    {
        if(!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken))
        {
            CustomUserDetails myUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().
                    getAuthentication().getPrincipal();
            if(likeService.likeListByItemAndUser(itemId,myUserDetails.getUser().getId())==null) return true;
            else return false;
        }
        else return false;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping (value="/profile/{uid}/collections/{id}/view_item/{item_id}/add_like", method={RequestMethod.POST,RequestMethod.GET})
    public String addLike(@PathVariable("item_id") int itemId,Like like) {
        CustomUserDetails myUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        Like likeExisting=likeService.likeListByItemAndUser(itemId,myUserDetails.getUser().getId());
        if(likeExisting==null)
        {
            like.setUser(myUserDetails.getUser());
            like.setItem(itemService.get(itemId));
            likeService.save(like);
        }
        else {
            likeService.delete(like);
            likeService.delete(likeExisting);
        }
        return "redirect:/profile/{uid}/collections/{id}/view_item/{item_id}";
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping (value="/profile/{uid}/collections/{id}/view_item/{item_id}/add_comment", method={RequestMethod.POST,RequestMethod.GET})
    public String addComment(@PathVariable("item_id") int itemId,Comment comment)
    {
        CustomUserDetails myUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        comment.setUser(myUserDetails.getUser());
        comment.setItem(itemService.get(itemId));
        comment.setCreationDate(new Timestamp(System.currentTimeMillis()));
        commentService.save(comment);
        return "redirect:/profile/{uid}/collections/{id}/view_item/{item_id}";
    }

    @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
    @PostMapping("/profile/{uid}/collections/{id}/add_item")
    public String addItem(@PathVariable("id") int id, @PathVariable("uid") Long uid,
                          @RequestParam("otherTag") String otherTag,@RequestParam("item_id") int itemId, Item item){
        System.out.println(itemId);
        if(otherTag!=""){
            List<String> tagStringList = Arrays.asList(otherTag.split("\\s*,\\s*"));
            Tag temporaryTag;
            Set<Tag> tags = new HashSet<>();
            for (String element : tagStringList) {
                temporaryTag = tagService.saveTag(element);
                tags.add(temporaryTag);
            }
            item.setTags(tags);
        }
        item.setCollection(collectionService.get(id));
        item.setId(itemId);
        itemService.update(item);
        return "redirect:/profile/"+uid+"/collections/"+ id + "/view";
    }

    @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
    @GetMapping("/profile/{uid}/collections/{id}/edit_item/{item_id}")
    public String editItem(@PathVariable("id") int id,@PathVariable("item_id") int itemId, @PathVariable("uid") Long uid,Model model){
        model.addAttribute("filter", new Filter());
        Collection collection = collectionService.get(id);
        Item item = itemService.get(itemId);
        Set<Tag> tags = item.getTags();
        List<String> stringList = new ArrayList<>();
        for (Tag element : tags) {
            stringList.add(element.getName());
        }
        model.addAttribute("collection", collection);
        model.addAttribute("item", item);
        model.addAttribute("other", String.join(",", stringList));
        return "edit_item";
    }

    @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
    @PostMapping("/profile/{uid}/collections/{id}/save_item/{item_id}")
    public String saveItem(@PathVariable("id") int id, @PathVariable("uid") Long uid,@PathVariable("item_id") int itemId,
                           Item item,@RequestParam String otherTag){
        if(otherTag!=""){
            List<String> tagStringList = Arrays.asList(otherTag.split("\\s*,\\s*"));
            Tag temporaryTag;
            Set<Tag> tags = item.getTags();
            for (String element : tagStringList) {
                temporaryTag = tagService.saveTag(element);
                tags.add(temporaryTag);
            }
            item.setTags(tags);
        }
        item.setId(itemId);
        item.setCollection(collectionService.get(id));
        itemService.update(item);
        return "redirect:/profile/"+uid+"/collections/"+ id + "/view";
    }

    @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
    @GetMapping("/profile/{uid}/collections/{id}/delete_item/{item_id}")
    public String deleteItemConfirmation(@PathVariable("id") int id,@PathVariable("item_id") int itemId, @PathVariable("uid") Long uid, Model model){
        model.addAttribute("filter", new Filter());
        Collection collection = collectionService.get(id);
        Item item = itemService.get(itemId);
        model.addAttribute("collection", collection);
        model.addAttribute("item", item);
        return "delete_item";
    }

    @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
    @PostMapping("/profile/{uid}/collections/{id}/delete_item/{item_id}")
    public String deleteItem(@PathVariable("id") int id, @PathVariable("uid") Long uid,@PathVariable("item_id") int itemId){

        itemService.delete(itemId);
        return "redirect:/profile/"+uid+"/collections/"+ id + "/view";
    }

    @PreAuthorize("hasRole('ADMIN') or #uid == authentication.principal.getId()")
    @RequestMapping(value = "/profile/{uid}/collections/{id}/action", method = {RequestMethod.POST,RequestMethod.GET}, params = "delete")
    public String deleteItems(@RequestParam(value="ids", required = false) List <Integer> ids, @PathVariable("id") int id, @PathVariable("uid") Long uid){
        if (ids != null)
            for (Integer itemId : ids)
            {
                Set<Tag> tags=itemService.get(itemId).getTags();
                for(Tag tag:tags)
                    if (itemService.listItemsByTagId(tag.getId()).size()==1)
                        tagService.deleteTag(tag.getId());
                itemService.delete(itemId);
            }
        return "redirect:/profile/"+uid+"/collections/"+ id + "/view";
    }

    @RequestMapping(value="/tagsAutocomplete")
    @ResponseBody
    public List<Tag> tagAutocomplete(@RequestParam(value="term", required = false, defaultValue="") String term)  {
        List<Tag> suggestions = new ArrayList<Tag>();
        try {
            // only update when term is three characters.
            if (term.length() == 3) {
                suggestions = tagService.fetchTags(term);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return suggestions;

    }

}
