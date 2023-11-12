package com.checkcheck.ecoreading.domain.delivery.entity;

public enum DeliveryPlace {
    // 수거/배송 장소(문앞, 경비실, 무인택배함)
    문앞,
    경비실,
    무인택배함;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private DeliveryPlace place;

        public Builder 문앞() {
            this.place = DeliveryPlace.문앞;
            return this;
        }

        public Builder 경비실() {
            this.place = DeliveryPlace.경비실;
            return this;
        }

        public Builder 무인택배함() {
            this.place = DeliveryPlace.무인택배함;
            return this;
        }

        public DeliveryPlace build() {
            return this.place;
        }
    }
}
