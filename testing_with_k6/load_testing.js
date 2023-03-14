import http from 'k6/http';
import { check } from 'k6';

export const options = {
    thresholds: {
        http_req_failed: ['rate<0.01'], // http errors should be less than 1%
        http_req_duration: ['p(95)<200'], // 95% of requests should be below 200ms
    },
    scenarios: {
        my_scenario1: {
            executor: 'constant-arrival-rate',
            duration: '3m',                    // total duration
            preAllocatedVUs: 50,                // to allocate runtime resources

            rate: 50,                           // number of constant iterations given `timeUnit`
            timeUnit: '1s',
        },
    },
};

export default function () {
    const res = http.get('http://node28791-env-6815870.rag-cloud.hosteur.com/dev-poc-siu/hospitals/51.437195/-2.847193');
    check(res, {
        'is status 200': (r) => r.status === 200,
    });
}
