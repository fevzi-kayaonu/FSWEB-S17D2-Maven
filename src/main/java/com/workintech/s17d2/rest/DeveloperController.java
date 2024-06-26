package com.workintech.s17d2.rest;

import com.workintech.s17d2.dto.DeveloperResponse;
import com.workintech.s17d2.model.*;
import com.workintech.s17d2.tax.Taxable;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.*;

@RestController
@RequestMapping("/developers")
public class DeveloperController {

    public final Map<Integer, Developer> developers = new HashMap<>();
    private Taxable taxable;



    @Autowired
    public DeveloperController(Taxable taxable) {
        this.taxable = taxable;
    }

    @PostConstruct
    public void init() {
        developers.put(1, new SeniorDeveloper(1, "Initial Developer", 1000d));
    }

    @GetMapping
    public List<Developer> getAll() {
        return developers.values().stream().toList();
    }

    @GetMapping("/{id}")
    public DeveloperResponse getById(@PathVariable("id") int id) {
        Developer foundDeveloper = this.developers.get(id);
        if(foundDeveloper==null){
            return new DeveloperResponse(foundDeveloper,HttpStatus.OK.value(),id+"ile search yapıldığında kayıt bulunamadı");
        }
        return new DeveloperResponse(foundDeveloper,HttpStatus.OK.value(),id+"ile search başarılı");
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeveloperResponse save(@RequestBody Developer developer){
        Developer createdDeveloper = DeveloperFactory.creatDeveloper(developer,taxable);
        if(Objects.nonNull(createdDeveloper)){
            developers.put(createdDeveloper.getId(),createdDeveloper);
        }
        return new DeveloperResponse(createdDeveloper, HttpStatus.CREATED.value(),"creat işlemi başarılı");
    }


    @PutMapping("/{id}")
    public DeveloperResponse update(@PathVariable("id") int id, @RequestBody Developer developer) {
        developer.setId(id);
        Developer newDeveloper = DeveloperFactory.creatDeveloper(developer, taxable);
        this.developers.put(id, newDeveloper);
        return new DeveloperResponse(newDeveloper, HttpStatus.OK.value(), "uptade başarılı.");

    }

    @DeleteMapping("/{id}")
    public DeveloperResponse delete(@PathVariable("id") int id) {
        Developer remowedDeveloper = this.developers.get(id);
        this.developers.remove(id);
        return new DeveloperResponse(remowedDeveloper,HttpStatus.NO_CONTENT.value(), "silme başarılı");
    }
}
