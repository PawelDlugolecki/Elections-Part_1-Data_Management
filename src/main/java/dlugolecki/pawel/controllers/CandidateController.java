package dlugolecki.pawel.controllers;

import dlugolecki.pawel.dto.CandidateDto;
import dlugolecki.pawel.service.CandidateService;
import dlugolecki.pawel.service.ConstituencyService;
import dlugolecki.pawel.service.PoliticalPartyService;
import dlugolecki.pawel.validators.CandidateValidator;
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
@RequestMapping("/candidates")
public class CandidateController {
    private final CandidateService candidateService;
    private final CandidateValidator candidateValidator;
    private final PoliticalPartyService politicalPartyService;
    private final ConstituencyService constituencyService;


    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.setValidator(candidateValidator);
    }

    @GetMapping("/add")
    public String addCandidateGET(Model model) {
        model.addAttribute("candidate", new CandidateDto());
        model.addAttribute("constituencies", constituencyService.getAllConstituencies());
        model.addAttribute("parties", politicalPartyService.getAllPoliticalParties());
        model.addAttribute("errors", new HashMap<>());
        return "candidates/add";
    }

    @PostMapping("/add")
    public String addCandidatePOST(@Valid @ModelAttribute CandidateDto candidateDto,
                                      BindingResult bindingResult,
                                      Model model) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getCode));

            model.addAttribute("candidate", candidateDto);
            model.addAttribute("constituencies", constituencyService.getAllConstituencies());
            model.addAttribute("parties", politicalPartyService.getAllPoliticalParties());
            model.addAttribute("errors", errors);
            return "candidates/add";
        }
        candidateService.addCandidate(candidateDto);
        return "redirect:/candidates";
    }

    @GetMapping
    public String getAllCandidates(Model model) {
        model.addAttribute("candidates", candidateService.getAllCandidates());
        return "candidates/all";
    }

    @GetMapping("/{id}")
    public String getOneCandidate(@PathVariable Long id, Model model) {
        model.addAttribute("candidate", candidateService.getOneCandidate(id));
        return "candidates/one";
    }

    @PostMapping("/delete")
    public String deleteCandidate(@RequestParam Long id) {
        candidateService.deleteCandidate(id);
        return "redirect:/candidates";
    }
}