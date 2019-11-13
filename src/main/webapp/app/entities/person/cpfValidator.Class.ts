export class CpfValidator {

    constructor() { }

    public checkCPF(strCPF: string): boolean {
        if (strCPF === null) {
            return false;
        }
        if (strCPF.length !== 11) {
            return false;
        }
        if ((strCPF === '00000000000') || (strCPF === '11111111111') || (strCPF === '22222222222') ||
            (strCPF === '33333333333') || (strCPF === '44444444444') || (strCPF === '55555555555') ||
            (strCPF === '66666666666') || (strCPF === '77777777777') || (strCPF === '88888888888') || (strCPF === '99999999999')) {
            return false;
        }
        let numero = 0;
        let caracter = '';
        const numeros = '0123456789';
        let j = 10;
        let somatorio = 0;
        let resto = 0;
        let digito1 = 0;
        let digito2 = 0;
        let strCPFAux = '';
        strCPFAux = strCPF.substring(0, 9);
        for (let i = 0; i < 9; i++) {
            caracter = strCPFAux.charAt(i);
            if (numeros.search(caracter) === -1) {
                return false;
            }
            numero = Number(caracter);
            somatorio = somatorio + (numero * j);
            j--;
        }
        resto = somatorio % 11;
        digito1 = 11 - resto;
        if (digito1 > 9) {
            digito1 = 0;
        }
        j = 11;
        somatorio = 0;
        strCPFAux = strCPFAux + digito1;
        for (let i = 0; i < 10; i++) {
            caracter = strCPFAux.charAt(i);
            numero = Number(caracter);
            somatorio = somatorio + (numero * j);
            j--;
        }
        resto = somatorio % 11;
        digito2 = 11 - resto;
        if (digito2 > 9) {
            digito2 = 0;
        }
        strCPFAux = strCPFAux + digito2;
        if (strCPF !== strCPFAux) {
            return false;
        }
        if (strCPF === strCPFAux) {
            return true;
        }
    }
}
