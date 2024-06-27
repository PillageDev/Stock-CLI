package dev.dynamic.commands;

public class CompanyProfile extends Command {
    @Override
    public String getCommand() {
        return "company";
    }

    @Override
    public String getDescription() {
        return "Get a company's profile by stock symbol";
    }

    @Override
    public void execute(String[] args) {
        System.out.println("Getting company profile for symbol: " + args[0]);
        dev.dynamic.api.CompanyProfile companyProfile = stockData.getCompanyProfile(args[0]);

        if (companyProfile == null) {
            System.out.println("No company profile found for symbol: " + args[0]);
            return;
        }

        System.out.println("Company Name: " + companyProfile.getName());
        System.out.println("Symbol: " + companyProfile.getTicker());
        System.out.println("Exchange: " + companyProfile.getExchange());
        System.out.println("Industry: " + companyProfile.getFinnhubIndustry());
        System.out.println("Web URL: " + companyProfile.getWeburl());
        System.out.println("Logo URL: " + companyProfile.getLogo());
        System.out.println("Phone: " + companyProfile.getPhone());
        System.out.println("Country: " + companyProfile.getCountry());
        System.out.println("Currency: " + companyProfile.getCurrency());
        System.out.println("Market Capitalization: " + companyProfile.getMarketCapitalization());
        System.out.println("IPO: " + companyProfile.getIpo());
        System.out.println("Shares Outstanding: " + companyProfile.getShareOutstanding());
    }

    @Override
    public String[] getRequiredArgs() {
        return new String[] { "symbol" };
    }

    @Override
    public String[] getOptionalArgs() {
        return new String[0];
    }

    @Override
    public String[] getAliases() {
        return new String[] { "profile", "info" };
    }
}
