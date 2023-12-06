package edu.depaul.amenities.controller;

import edu.depaul.amenities.model.AmenityDTO;
import edu.depaul.amenities.model.AmenityType;
import edu.depaul.amenities.service.AmenityService;
import edu.depaul.amenities.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/amenities")
public class AmenityController {

    private final AmenityService amenityService;

    public AmenityController(final AmenityService amenityService) {
        this.amenityService = amenityService;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("typeValues", AmenityType.values());
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("amenities", amenityService.findAll());
        return "amenity/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("amenity") final AmenityDTO amenityDTO) {
        return "amenity/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("amenity") @Valid final AmenityDTO amenityDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("name") && amenityService.nameExists(amenityDTO.getName())) {
            bindingResult.rejectValue("name", "Exists.amenity.name");
        }
        if (bindingResult.hasErrors()) {
            return "amenity/add";
        }
        amenityService.create(amenityDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("amenity.create.success"));
        return "redirect:/amenities";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("amenity", amenityService.get(id));
        return "amenity/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("amenity") @Valid final AmenityDTO amenityDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final AmenityDTO currentAmenityDTO = amenityService.get(id);
        if (!bindingResult.hasFieldErrors("name") &&
                !amenityDTO.getName().equalsIgnoreCase(currentAmenityDTO.getName()) &&
                amenityService.nameExists(amenityDTO.getName())) {
            bindingResult.rejectValue("name", "Exists.amenity.name");
        }
        if (bindingResult.hasErrors()) {
            return "amenity/edit";
        }
        amenityService.update(id, amenityDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("amenity.update.success"));
        return "redirect:/amenities";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        amenityService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("amenity.delete.success"));
        return "redirect:/amenities";
    }

}
