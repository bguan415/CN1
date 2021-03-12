public enum Size{
        S(0), M(1), L(2), XL(3);
        
        private int value;

        private Size(int value){
            this.value = value;
        }
        
        public String toString(){
            switch(value) {
                case 0: return "S";
                case 1: return "M";
                case 2: return "L";
                case 3: return "XL";
                default: return "S";
            }
        }
}
