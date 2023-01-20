
export namespace PhoneNumber {
    const mobileNumbers = ["45", "50", "51", "53", "57", "60", "66", "69", "72", "73", "78", "79", "88"]
    const VoIPNumbers = ["39"]
    const fixedLineNumbers = [
        ["12", "13", "14", "15", "16", "17", "18"],
        ["22", "23", "24", "25", "29"],
        ["32", "33", "34"],
        ["41", "42", "43", "44", "46", "48"],
        ["52", "54", "55", "56", "58", "59"],
        ["61", "62", "63", "65", "67", "68"],
        ["71", "74", "75", "76", "77"],
        ["81", "82", "83", "84", "85", "86", "87", "89"],
        ["91", "94", "95"]
    ].flat()
    const ministryNumbers = ["26", "47"]

    export function toString(phoneNumber: number) {
        if (Math.floor(phoneNumber) !== phoneNumber) return
        const ph = phoneNumber.toString()
        if (ph.length !== 9) return
        const sl = ph.slice(0, 2)
        switch (true) {
            case mobileNumbers.includes(sl) ||
                VoIPNumbers.includes(sl):
                return toStringMobile(ph)
            case fixedLineNumbers.includes(sl) ||
                ministryNumbers.includes(sl):
                return toStringLine(ph)
        }
    }
    function toStringMobile(ph: string) {
        return `${ph.slice(0, 3)} ${ph.slice(3, 6)} ${ph.slice(6, 9)}`
    }

    function toStringLine(ph: string) {
        return `${ph.slice(0, 2)} ${ph.slice(2, 5)} ${ph.slice(5, 7)} ${ph.slice(7, 9)}`
    }
}




