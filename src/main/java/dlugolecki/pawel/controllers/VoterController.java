package dlugolecki.pawel.controllers;

import dlugolecki.pawel.dto.VoterDto;
import dlugolecki.pawel.model.enums.Education;
import dlugolecki.pawel.model.enums.Gender;
import dlugolecki.pawel.service.ConstituencyService;
import dlugolecki.pawel.service.TokenService;
import dlugolecki.pawel.service.VoterService;
import dlugolecki.pawel.validators.VoterValidator;
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
@RequestMapping("/voters")
public class VoterController {

    private final VoterService voterService;
    private final VoterValidator voterValidator;
    private final TokenService tokenService;
    private final ConstituencyService constituencyService;

    @InitBinder(value = "/voters/showToken")
    private void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(voterValidator);
    }


    @GetMapping("/add")
    public String addVoterGET(Model model) {
        model.addAttribute("voter", new VoterDto());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("education", Education.values());
        model.addAttribute("constituencies", constituencyService.getAllConstituencies());
        model.addAttribute("errors", new HashMap<>());
        return "voters/add";
    }

    @PostMapping("/showToken")
    public String showToken(@Valid @ModelAttribute VoterDto voterDto,
                            BindingResult bindingResult,
                            Model model) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getCode));

            model.addAttribute("voter", voterDto);
            model.addAttribute("genders", Gender.values());
            model.addAttribute("education", Education.values());
            model.addAttribute("constituencies", constituencyService.getAllConstituencies());
            model.addAttribute("errors", errors);
            return "voters/add";
        }
        VoterDto voter = voterService.addVoter(voterDto);
        model.addAttribute("token", tokenService.getTokenById(voter.getId()));
        return "voters/showToken";
    }
}
