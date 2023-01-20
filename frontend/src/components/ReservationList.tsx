import axios from 'axios';
import { ReactElement, useState } from 'react';
import { useQuery } from 'react-query';

import { baseUrl } from '../App';
import { reservation, reservationsKey } from '../types';
import ReservationCard from './ReservationCard';

type propT = {
    accessToken: string
}

export default function ReservationList({ accessToken }: propT): ReactElement {

    const { data, isLoading } = useQuery(reservationsKey + accessToken, async () => {
        return (await axios.get<reservation[]>(`${baseUrl}/dining_tables/reservations`, {
            headers: { Authorization: `Bearer ${accessToken}` }
        })).data
    }, {
        staleTime: 60 * 60 * 1000
    })

    const [filter, setFilter] = useState([] as string[])

    const dispData = data?.filter(e => !filter.includes(e.removalToken))

    return <section className="flex flex-col gap-y-2">
        {isLoading ? <p className="w-8 h-8 animate-spin">⌛</p> : dispData?.map((r) => <ReservationCard key={r.removalToken} reservation={r} onCancel={r => setFilter(f => f.concat(r.removalToken))} />)}
    </section>
}
