package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.groups.Default;

public interface ValidationGroup {

    interface OnCreate extends Default {
    }

    interface OnUpdate extends Default {
    }
}