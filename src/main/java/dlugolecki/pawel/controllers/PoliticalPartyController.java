package dlugolecki.pawel.controllers;

import dlugolecki.pawel.dto.PoliticalPartyDto;
import dlugolecki.pawel.service.PoliticalPartyService;
import dlugolecki.pawel.validators.PoliticalPartyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/politicalParties")
public class PoliticalPartyController {

    private final PoliticalPartyService politicalPartyService;
    private final PoliticalPartyValidator politicalPartyValidator;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.setValidator(politicalPartyValidator);
    }

    @GetMapping("/add")
    public String addPoliticalPartyGET(Model model) {
        model.addAttribute("politicalParty", new PoliticalPartyDto());
        model.addAttribute("errors", new HashMap<>());
        return "politicalParties/add";
    }

    @PostMapping("/add")
    public String addPoliticalPartyPOST(@Valid @ModelAttribute PoliticalPartyDto politicalPartyDto,
                                        BindingResult bindingResult,
                                        Model model) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors()
                    .stream().collect(Collectors.toMap(FieldError::getField, FieldError::getCode));

            model.addAttribute("politicalParty", politicalPartyDto);
            model.addAttribute("errors", errors);
            return "politicalParties/add";
        }
        politicalPartyService.addPoliticalParty(politicalPartyDto);
        return "redirect:/politicalParties";
    }

    @GetMapping
    public String getAllPoliticalParties(Model model) {
        model.addAttribute("politicalParties", politicalPartyService.getAllPoliticalParties());
        return "politicalParties/all";
    }

    @PostMapping("/delete")
    public String deletePoliticalParty(@RequestParam Long id) {
        politicalPartyService.deletePoliticalParty(id);
        return "redirect:/politicalParties";
    }
}