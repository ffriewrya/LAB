//package ru.itmo.moona;
//
//public class ReagentValidator {
//    public void validate(Reagent reg) {
//        if (reg.getName() == null || reg.getName().isEmpty() || reg.getName().isBlank() || reg.getName().length() >= 128) {
//            throw new IllegalArgumentException("...name cannot be blank OR above 128 symbols");
//        }
//        if (reg.getFormula() != null && reg.getFormula().length() > 32) {
//            throw new IllegalArgumentException("this formula is just too much");
//        }
//        if (reg.getCas() != null && reg.getCas().length() > 32) {
//            throw new IllegalArgumentException("this cas is wrong..");
//        }
//        if (reg.getHazardClass() != null && reg.getHazardClass().length() > 32) {
//            throw new IllegalArgumentException("this thing is WAY too... hazardous??");
//        }
//    }
//}
