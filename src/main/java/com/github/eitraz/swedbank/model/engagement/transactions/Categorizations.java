package com.github.eitraz.swedbank.model.engagement.transactions;

import com.github.eitraz.swedbank.model.Links;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@SuppressWarnings("WeakerAccess")
@Getter
@Setter
public class Categorizations {
    private Links links;
    private List<Category> categories;
}
